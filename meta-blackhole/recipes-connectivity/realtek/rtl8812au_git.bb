SUMMARY = "Driver for Realtek 8812AU USB wireless devices"
HOMEPAGE = "http://www.realtek.com/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1b3e79661a0d2a7f0ad9ccc86c20854e"

PR = "r0"
PR .= "-bh0"

SRC_REV = "0d97feaa9b72c836061094f17170658a56a0aec4"
BRANCH = "master"

SRC_URI = "git://github.com/BlackHole/rtl8812AU_8821AU_linux.git;protocol=git;tag=${SRC_REV};branch=${BRANCH} \
	"

S = "${WORKDIR}/git"

inherit module siteinfo

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake -C "${STAGING_KERNEL_DIR}" M="${S}" modules 
}

EXTRA_OEMAKE = "CONFIG_RTL8812AU_8821AU=m"

do_install() {
        install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
        install -m 0644 ${S}/8812au.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
}

