###############################################################################
#                      FULL BACKUP UYILITY FOR  VU+                           #
#        Tools original by scope34 with additions by Dragon48 & DrData        #
#               modified by Pedro_Newbie (pedro.newbie@gmail.com)             #
#                       modified by meo & dpeddi                              #
###############################################################################
#
#!/bin/sh

START=$(date +%s)

if [ $# = 0 ]; then
	echo "Error: missing target device specification"
	echo "       mount the target device from blue panel before running this tool"
	exit
fi

DIRECTORY=$1
DATE=`date +%Y%m%d_%H%M`
IMAGEVERSION=`date +%Y%m%d`
if grep rootfs /proc/mounts | grep ubifs > /dev/null; then	# TESTING FOR UBIFS
	ROOTFSTYPE=ubifs
else
	ROOTFSTYPE=jffs2										# NO UBIFS THEN JFFS2
fi
MKFS=/usr/sbin/mkfs.$ROOTFSTYPE
UBINIZE=/usr/sbin/ubinize
NANDDUMP=/usr/sbin/nanddump
WORKDIR=$DIRECTORY/bi
TARGET="XX"

if [ -f /proc/stb/info/vumodel ] ; then
	MODEL=$( cat /proc/stb/info/vumodel )
	TYPE=VU
	SHOWNAME="Vu+ ${MODEL}"
	MAINDEST=$DIRECTORY/vuplus/${MODEL}
	EXTRA=$DIRECTORY/fullbackup_${MODEL}/$DATE/vuplus	
	if [ $ROOTFSTYPE = "ubifs" ] ; then
		MKUBIFS_ARGS="-m 2048 -e 126976 -c 4096 -F"
		UBINIZE_ARGS="-m 2048 -p 128KiB"
	else
		MTDROOT=0
		MTDBOOT=2
		JFFS2OPTIONS="--eraseblock=0x20000 -n -l"
	fi
else
	echo "No supported receiver found!"
	exit 0
fi

## START THE REAL BACK-UP PROCESS
echo "$SHOWNAME" | tr  a-z A-Z
echo "BACK-UP TOOL, FOR MAKING A COMPLETE BACK-UP"

echo " "
echo "Please be patient, ... will take about 5-7 minutes for this system."

echo " "

## TESTING IF ALL THE TOOLS FOR THE BUILDING PROCESS ARE PRESENT
if [ ! -f $MKFS ] ; then
	echo $MKFS; echo "not found."
	exit 0
fi
if [ ! -f $NANDDUMP ] ; then
	echo $NANDDUMP ;echo "not found."
	exit 0
fi

## PREPARING THE BUILDING ENVIRONMENT
rm -rf $WORKDIR
mkdir -p $WORKDIR
mkdir -p /tmp/bi/root
sync
mount --bind / /tmp/bi/root

if [ $ROOTFSTYPE = "jffs2" ] ; then 
	echo "Create: root.jffs2"
	$MKFS --root=/tmp/bi/root --faketime --output=$WORKDIR/root.jffs2 $JFFS2OPTIONS
else
	echo "Create: root.ubifs"
	echo \[ubifs\] > $WORKDIR/ubinize.cfg
	echo mode=ubi >> $WORKDIR/ubinize.cfg
	echo image=$WORKDIR/root.ubi >> $WORKDIR/ubinize.cfg
	echo vol_id=0 >> $WORKDIR/ubinize.cfg
	echo vol_type=dynamic >> $WORKDIR/ubinize.cfg
	echo vol_name=rootfs >> $WORKDIR/ubinize.cfg
	echo vol_flags=autoresize >> $WORKDIR/ubinize.cfg
	touch $WORKDIR/root.ubi
	chmod 644 $WORKDIR/root.ubi
	#cp -ar /tmp/bi/root $WORKDIR/root
	#$MKFS -r $WORKDIR/root -o $WORKDIR/root.ubi $MKUBIFS_ARGS
	$MKFS -r /tmp/bi/root -o $WORKDIR/root.ubi $MKUBIFS_ARGS || rm $WORKDIR/root.ubi
	$UBINIZE -o $WORKDIR/root.ubifs $UBINIZE_ARGS $WORKDIR/ubinize.cfg || rm $WORKDIR/root.ubifs
fi
chmod 644 $WORKDIR/root.$ROOTFSTYPE

echo "Create: kerneldump"
kernelmtd=$(cat /proc/mtd  | grep kernel | cut -d\: -f1)
nanddump /dev/$kernelmtd -q > $WORKDIR/vmlinux.gz || rm $WORKDIR/vmlinux.gz

unset REBOOT_UPDATE
unset FORCE_UPDATE

case ${MODEL} in
	solo2)
		ROOTFS_EXT=bin
		INITRD=initrd_cfe_auto.bin

		#CFE> show devices
		#Device Name          Description
		#-------------------  ---------------------------------------------------------
		#              uart0  16550 DUART at 0xB0406900 channel 0
		#            uart_b0  16550 DUART B at 0xB0406940 channel 0
		#         flash0.cfe  New NAND flash at 00000000 offset 00000000 size 2048KB spare 256KB
		#      flash0.kernel  New NAND flash at 00000000 offset 00200000 size 7168KB spare 640KB
		#      flash0.macadr  New NAND flash at 00000000 offset 00900000 size 1024KB spare 128KB
		#       flash0.nvram  New NAND flash at 00000000 offset 00A00000 size 1024KB spare 128KB
		#     flash0.virtual  New NAND flash at 00000000 offset 00B00000 size 1024KB spare 128KB
		#     flash0.kreserv  New NAND flash at 00000000 offset 00C00000 size 1024KB spare 128KB
		#      flash0.splash  New NAND flash at 00000000 offset 00D00000 size 2048KB spare 640KB
		#      flash0.initrd  New NAND flash at 00000000 offset 00F00000 size 16384KB spare 7168KB
		#      flash0.avail0  New NAND flash at 00000000 offset 01F00000 size 230400KB spare 2048KB
		#               eth0  GENET Internal Ethernet at 0xB0430800

		#solo2 (and perhaps duo2) have no fixed mtd partition
		#we found address in full mtd where is stored initrd and splash and dump them using dd and skip.
		entire_devicemtd=$(grep "entire_device" /proc/mtd | cut -d\: -f1)
		if [ x${entire_devicemtd} != x ]; then
			echo "Create: splashdump"
			#0x00D00000 1024*2
			dd if=/dev/${entire_devicemtd} of=$WORKDIR/splash.dump skip=13 bs=1048576 count=2 || rm $WORKDIR/splash.dump
			file $WORKDIR/splash.dump 2>/dev/null | grep -q "PC bitmap data"
			if [ $? != 0 ]; then
				echo "Splash dump is not a bitmap.. skip"
				rm $WORKDIR/splash.dump
			fi

			#0x00F00000 1024*16
			echo "Create: initrddump"
			dd if=/dev/${entire_devicemtd} of=$WORKDIR/initrd.dump skip=15 bs=1048576 count=16 || rm $WORKDIR/initrd.dump
			gzip -t $WORKDIR/initrd.dump 2>/dev/null
			gzip_rc=$?
			if [ ${gzip_rc} != 0 ]; then
				#pre production system have initrd at different location
				dd if=/dev/${entire_devicemtd} of=$WORKDIR/initrd.dump skip=14 bs=1048576 count=16 || rm $WORKDIR/initrd.dump
				gzip -t $WORKDIR/initrd.dump 2>/dev/null
				gzip_rc=$?
			fi
			if [ ${gzip_rc} != 0 ]; then
				echo "Can't get initrd location.. skip"
				rm $WORKDIR/initrd.dump
			fi
		fi

		REBOOT_UPDATE=yes
	;;
	duo2)
		ROOTFS_EXT=bin
		INITRD=initrd_cfe_auto.bin
		REBOOT_UPDATE=yes
	;;
	solose)
		ROOTFS_EXT=bin
		INITRD=initrd_cfe_auto.bin
		FORCE_UPDATE=yes
	;;
	zero)
		ROOTFS_EXT=bin
		INITRD=initrd_cfe_auto.bin
		FORCE_UPDATE=yes
	;;
	*)
		ROOTFS_EXT=jffs2

		echo "Create: splashdump"
		splashmtd=$(cat /proc/mtd  | grep splash | cut -d\: -f1)
		if [ x$splashmtd != x ]; then
			nanddump /dev/$splashmtd -q > $WORKDIR/splash.dump || rm $WORKDIR/splash.dump
		fi
	;;
esac

echo " "
echo "Almost there... Now building the USB-Image!"

## HANDLING THE VU+ SERIES
if [ $TYPE = "VU" ] ; then
	rm -rf $MAINDEST
	mkdir -p $MAINDEST
	#mkdir -p $EXTRA/${MODEL}

	SPLASH=splash_cfe_auto.bin

	if [ $ROOTFSTYPE = "ubifs" ] ; then
		mv $WORKDIR/root.ubifs $MAINDEST/root_cfe_auto.${ROOTFS_EXT} || rm $MAINDEST/root_cfe_auto.${ROOTFS_EXT}
	else
		mv $WORKDIR/root.jffs2 $MAINDEST/root_cfe_auto.jffs2 || rm $MAINDEST/kernel_cfe_auto.bin$MAINDEST/root_cfe_auto.jffs2
	fi
	mv $WORKDIR/vmlinux.gz $MAINDEST/kernel_cfe_auto.bin || rm $MAINDEST/kernel_cfe_auto.bin

	if [ -s $WORKDIR/splash.dump ]; then
		DOWNLOADSPLASH=0
		mv $WORKDIR/splash.dump $MAINDEST/$SPLASH || rm $MAINDEST/$SPLASH
	else
		DOWNLOADSPLASH=1
	fi

	if [ -s $WORKDIR/initrd.dump ]; then
		DOWNLOADINTRD=0
		mv $WORKDIR/initrd.dump $MAINDEST/$INITRD || rm $MAINDEST/$INITRD
	else
		DOWNLOADINTRD=1
	fi

	if [[ $DOWNLOADINTRD = 1 || $DOWNLOADSPLASH = 1 ]]; then
		case ${MODEL} in
		duo2|solo2|solose|zero)
			echo "Vu+ ${MODEL} don't expose some partitions, try getting it from internet"
			mkdir -p /tmp/backupfull-$$
			cd /tmp/backupfull-$$ && \
			opkg update && \
			opkg download blackhole-bootlogo && \
			ar x blackhole-bootlogo* && \
			tar zxvf data.tar.gz
			if [[ $DOWNLOADINTRD = 1 ]]; then
			    find . -name "initrd_cfe_auto.bin" -exec mv {} $MAINDEST/$INITRD \; 
			fi
			if [[ $DOWNLOADSPLASH = 1 ]]; then
			    find . -name "splash_cfe_auto.bin" -exec mv {} $MAINDEST/$SPLASH \; 
			fi
			cd - > /dev/null
			rm -rf /tmp/backupfull-$$
		;;
		esac
	fi

	if [ x$REBOOT_UPDATE = xyes ]; then
		touch $MAINDEST/reboot.update
		chmod 664 $MAINDEST/reboot.update
	fi

	if [ x$FORCE_UPDATE = xyes ]; then
		touch $MAINDEST/force.update
		chmod 664 $MAINDEST/force.update
	fi

	#cp -r $MAINDEST $EXTRA #copy the made back-up to images
	if [ -f $MAINDEST/root_cfe_auto.${ROOTFS_EXT} -a -f $MAINDEST/kernel_cfe_auto.bin ] ; then
		echo " "
		echo "USB Image created in:";echo $MAINDEST
		#echo "# and there is made an extra copy in:"
		#echo $EXTRA
		echo " "
		echo "To restore the image:"
		echo "Place the USB-flash drive in the (front) USB-port and switch the receiver off and on with the powerswitch on the back of the receiver."
		echo "Follow the instructions on the front-display."
		echo "Please wait.... almost ready!"

	else
		echo "Image creation FAILED!"
		echo "Probable causes could be:"
		echo "-> no space left on back-up device"
		echo "-> no writing permission on back-up device"
		echo " "
	fi
fi


umount /tmp/bi/root
rmdir /tmp/bi/root
rmdir /tmp/bi
rm -rf $WORKDIR
sleep 5
END=$(date +%s)
DIFF=$(( $END - $START ))
MINUTES=$(( $DIFF/60 ))
SECONDS=$(( $DIFF-(( 60*$MINUTES ))))
echo "Time required for this process:" ; echo "$MINUTES:$SECONDS"
exit 