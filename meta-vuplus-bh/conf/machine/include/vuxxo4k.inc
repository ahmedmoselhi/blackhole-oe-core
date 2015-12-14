# We know about rtc
MACHINE_FEATURES_BACKFILL_CONSIDERED = "rtc"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS = " \
	${@base_contains("MACHINE_FEATURES", "dvbproxy", "vuplus-dvb-proxy-${MACHINE} vuplus-libgles-${MACHINE} vuplus-platform-util-${MACHINE} libvugles2", "vuplus-dvb-modules-${MACHINE}", d)} \
	gst-ffmpeg \
"

DVBPROVIDER = "kernel"

KERNEL_MODULE_AUTOLOAD += "xfs"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS = "\
    kernel-module-cdfs \
    vuplus-tuner-turbo \
    "

MACHINE_EXTRA_RRECOMMENDS = " \
	vuplus-shutdown \
	gst-plugin-dvbmediasink \
	ntfs-3g \
	${@base_contains("MACHINE_FEATURES", "minitv", "enigma2-plugin-extensions-minitv", "", d)} \
"

include conf/machine/include/autoload-filesystems.inc

EXTRA_IMAGEDEPENDS += "\
	enigma2-plugin-systemplugins-remotecontrolcode \
	enigma2-plugin-extensions-addstreamurl \
"

TARGET_ARCH = "arm"
DEFAULTTUNE = "armv7ahf-neon"

DVBMEDIASINK_CONFIG = "--with-wmv --with-pcm --with-eac3"

PREFERRED_VERSION_linux-${MACHINE} = "3.14.28"

PREFERRED_PROVIDER_virtual/kernel = "linux-vuplus"

PREFERRED_PROVIDER_virtual/blindscan-dvbc = "vuplus-blindscan-utils-${MACHINE}"
PREFERRED_PROVIDER_virtual/blindscan-dvbs = "vuplus-blindscan-utils-${MACHINE}"
PREFERRED_PROVIDER_virtual/transtreamproxy = "vuplus-transtreamproxy"
PREFERRED_PROVIDER_virtual/xmbc = "vuplus-xbmc-${MACHINE}"

KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "${KERNEL_IMAGETYPE}"
KERNEL_CONSOLE = "ttyS0,115200n8"

IMAGE_FSTYPES ?= "ubi"

UBI_VOLNAME = "rootfs"
MKUBIFS_ARGS = "-m 2048 -e 126976 -c ${MKFS_UBI_MAX_ERASE_BLK_COUNT} ${MKFS_EXTRA}"
UBINIZE_ARGS = "-m 2048 -p 128KiB"


IMAGEDIR ?= "vuplus/${UPDATE_DIR}"

IMAGE_CMD_ubi_prepend = " \
    rm -rf ${IMAGE_ROOTFS}/boot/*; \
    rm -rf ${IMAGE_ROOTFS}/tmp/*; \
    "


MACHINE_FEATURES += "3dtv alsa bootsplash usbhost wlan kernelwifi extrakernelwifi switchoff osdposition hdmicec"

BOOTLOGO_FILENAME = "splash_cfe_auto.bin"


require conf/machine/include/arm/arch-armv7a.inc

MACHINE_KERNEL_PR = "r1"

