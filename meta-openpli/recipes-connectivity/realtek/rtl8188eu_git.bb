SUMMARY = "Driver for Realtek 8188EU USB wireless devices"
HOMEPAGE = "http://www.realtek.com/"
LICENSE = "CLOSED"
PR = "r2"

PR .= "-bh3"

SRC_REV = "0cb491cae8362030658b778e57a35700b0780cf9"
BRANCH = "master"

SRC_URI = "git://github.com/lwfinger/rtl8188eu.git;protocol=git;tag=${SRC_REV};branch=${BRANCH} \
	"

S = "${WORKDIR}/git"

inherit module siteinfo

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake -C "${STAGING_KERNEL_DIR}" M="${S}" modules 
}

EXTRA_OEMAKE = "CONFIG_RTL8188EU=m"

FILES_${PN} += "${base_libdir}/firmware/rtlwifi/rtl8188eufw.bin"

do_install() {
        install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
        install -m 0644 ${S}/8188eu.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
	install -d ${D}/lib/firmware/rtlwifi
	install -m 0644 ${S}/rtl8188eufw.bin ${D}/lib/firmware/rtlwifi
}

