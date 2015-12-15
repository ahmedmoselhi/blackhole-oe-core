require ${OPENPLI_BASE}/meta-openpli/recipes-openpli/images/openpli-image.bb

WIFI_FIRMWARES = "\
	firmware-htc7010 \
	firmware-htc9271 \
	firmware-rt2870 \
	firmware-rt73 \
	firmware-rtl8712u \
	firmware-zd1211 \
	firmware-rt2561 \
	firmware-rt3070 \
"

KERNEL_WIFI_MODULES = " \
	kernel-module-ath9k-htc \
	kernel-module-carl9170 \
	kernel-module-prism2-usb \
	kernel-module-rt73usb \
	kernel-module-rt2500usb \
	kernel-module-rtl8187 \
	kernel-module-r8712u \
	kernel-module-w35und \
	kernel-module-zd1211rw \
	kernel-module-llc \
	kernel-module-stp \
	kernel-module-bridge \
	kernel-module-hostap \
	kernel-module-rt2800usb \
"

LEGACY_MODULES = " \
	rt3070 \
	r8192cu \
	rtl8812au \
	rtl8188eu \
"

ENIGMA2_PLUGINS = " \
	enigma2-plugin-systemplugins-crossepg \
	enigma2-plugin-extensions-dlnabrowser \
	enigma2-plugin-extensions-dlnaserver \
	enigma2-plugin-extensions-audiosync \
	enigma2-plugin-extensions-autobackup \
	enigma2-plugin-extensions-cutlisteditor \
	enigma2-plugin-extensions-graphmultiepg \
	enigma2-plugin-extensions-mediaplayer \
	enigma2-plugin-extensions-mediascanner \
	enigma2-plugin-extensions-openwebif \
	enigma2-plugin-extensions-pictureplayer \
	enigma2-plugin-systemplugins-fastscan \
	enigma2-plugin-systemplugins-hotplug \
	enigma2-plugin-systemplugins-networkbrowser \
	enigma2-plugin-systemplugins-positionersetup \
	enigma2-plugin-systemplugins-satfinder \
	enigma2-plugin-systemplugins-skinselector \
	enigma2-plugin-systemplugins-softwaremanager \
	enigma2-plugin-systemplugins-videomode \
	enigma2-plugin-systemplugins-autoresolution \
  	enigma2-plugin-systemplugins-commoninterfaceassignment \	
	enigma2-plugin-systemplugins-osd3dsetup \
	enigma2-plugin-systemplugins-hdmicec \
	enigma2-plugin-systemplugins-osdpositionsetup \
	enigma2-plugin-systemplugins-wirelesslan \
	${@base_contains("MACHINE_FEATURES", "dvb-c", "enigma2-plugin-systemplugins-cablescan" , "", d)} \
	${@base_contains("MACHINE_FEATURES", "uianimation", "enigma2-plugin-systemplugins-animationsetup" , "", d)} \
	${@base_contains("MACHINE_FEATURES", "blindscan-dvbs", "enigma2-plugin-systemplugins-vuplusblindscan", "", d)} \
	${@base_contains("MACHINE_FEATURES", "xbmc", "enigma2-plugin-extensions-xbmc", "", d)} \
	\
	${@base_conditional("MACHINE", "vusolo2", "enigma2-plugin-extensions-hbbtv", "", d)} \
	${@base_conditional("MACHINE", "vuduo2", "enigma2-plugin-extensions-hbbtv", "", d)} \
	${@base_conditional("MACHINE", "vusolose", "enigma2-plugin-extensions-hbbtv", "", d)} \
  	${@base_conditional("MACHINE", "vuzero", "enigma2-plugin-extensions-hbbtv", "", d)} \
	"

DVB_USB_DRIVERS = " \
	enigma2-plugin-drivers-dvb-usb-dib0700 \
	enigma2-plugin-drivers-dvb-usb-af9015 \
	enigma2-plugin-drivers-dvb-usb-af9035 \
	enigma2-plugin-drivers-dvb-usb-siano \
	enigma2-plugin-drivers-dvb-usb-em28xx \
	enigma2-plugin-drivers-dvb-usb-dw2102 \
	enigma2-plugin-drivers-dvb-usb-as102 \
	enigma2-plugin-drivers-dvb-usb-it913x \
	enigma2-plugin-drivers-dvb-usb-pctv452e \
	enigma2-plugin-drivers-dvb-usb-dtt200u \
	enigma2-plugin-drivers-dvb-usb-rtl2832 \
	kernel-module-dvb-usb-v2 \
	${@base_contains('PREFERRED_VERSION_linux-vuplus', '3.13.5', 'kernel-module-cypress-firmware', '', d)} \
	${@base_contains('PREFERRED_VERSION_linux-vuplus', '3.9.6', 'kernel-module-dvb-usb-cypress-firmware', '', d)} \
	"

IMAGE_INSTALL += " \
	3rd-party-feed-configs \
	aio-grab \
	blackhole-base \
	blackholesocker \
	inadyn \
	mc \
	openvpn \
	parted \
	enigma2 \
	libavahi-client \
	tuxbox-common \
	ntfs-3g \
	libcrypto-compat \
	${ENIGMA2_PLUGINS} \
	\
	${@base_contains("MACHINE_FEATURES", "transcoding", "streamproxy", "", d)} \
	${@base_contains('MACHINE_FEATURES', 'ctrlrc', "enigma2-plugin-systemplugins-remotecontrolcode", "", d)} \
	${WIFI_FIRMWARES} \
	${KERNEL_WIFI_MODULES} \
	${LEGACY_MODULES} \
	rt2870sta \
	${DVB_USB_DRIVERS} \
	\
	${@base_contains('MACHINE_FEATURES', 'dvd', 'cdfs cdtextinfo kernel-module-isofs kernel-module-udf', '', d)} \
	python-argparse \
	"

ENIGMA2_PLUGINS += " \
	enigma2-plugin-systemplugins-crossepg \
	"

export IMAGE_BASENAME = "openblackhole"
