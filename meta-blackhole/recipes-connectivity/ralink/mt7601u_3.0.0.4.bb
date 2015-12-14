DESCRIPTION = "Driver for Ralink mt7601u USB 802.11a/b/g/n/ac WiFi sticks"
SECTION = "kernel/modules"
LICENSE = "CLOSED"

inherit module

PR = "r0"
PR .= "-bh1"

SRC_URI = "file://DPO_MT7601U_LinuxSTA_3.0.0.4_20130913.tar.bz2 \
	file://${PN}/${PV}/change_device_name.patch;patch=1 \
	file://${PN}/${PV}/firmware_file_rename.patch;patch=1 \
	file://${PN}/${PV}/buildfix.patch;patch=1 \
	"

S = "${WORKDIR}/DPO_MT7601U_LinuxSTA_3.0.0.4_20130913"

EXTRA_OEMAKE = "LINUX_SRC=${STAGING_KERNEL_DIR}"

do_install() {
	install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
	install -d ${D}${sysconfdir}/Wireless/mt7601uSTA
	install -m 0644 ${S}/os/linux/mt7601Usta.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/mt7601Usta.ko
	install -m 0644 ${S}/RT2870STA.dat ${D}${sysconfdir}/Wireless/mt7601uSTA/mt7601uSTA.dat
	install -m 0644 ${S}/RT2870STACard.dat ${D}${sysconfdir}/Wireless/mt7601uSTA/mt7601uSTACard.dat
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI[md5sum] = "2b552aff1bbd4effe94185e222eb761e"
SRC_URI[sha256sum] = "c0061b9010b80c1fc09d78786317957044bde43e2a127ecefd66d4faa12d2906"

FILES_${PN} += "${sysconfdir}/Wireless/mt7601uSTA/mt7601uSTA.dat ${sysconfdir}/Wireless/mt7601uSTA/mt7601uSTACard.dat"
