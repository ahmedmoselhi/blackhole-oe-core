DESCRIPTION = "Hardware drivers for VuPlus"
SECTION = "base"
LICENSE = "CLOSED"
require conf/license/license-close.inc

PROVIDES += "vuplus-dvb-modules"
RPROVIDES_${PN} += "vuplus-dvb-modules"

MACHINEDRIVERSDOWNLOAD = "${MACHINE}"
MACHINEDRIVERSDOWNLOAD_vuduo = "bm750"

KV = "${VUPLUS_KERNEL_VERSION}"
PV = "${KV}"
PR = "r22-${SRCDATE}"

SRC_URI = "http://archive.vuplus.com/download/drivers/vuplus-dvb-modules-${MACHINEDRIVERSDOWNLOAD}-${PV}-${SRCDATE}.tar.gz "

S = "${WORKDIR}"

#inherit module-base
DEPENDS = "virtual/kernel"
KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_install() {
        install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
        for f in *.ko; do
                install -m 0644 ${WORKDIR}/$f ${D}/lib/modules/${KERNEL_VERSION}/extra/$f;
        done
	install -d ${D}/etc/modules-load.d
	echo -e "${MODULES_NAME}" > ${D}/etc/modules-load.d/${PN}.conf
	echo -e "${MODULES_NAME}" > ${D}/etc/modules
}

pkg_postinst_${PN} () {
        if [ -d /proc/stb ]; then
                depmod -a
        fi
        true
}

PACKAGE_ARCH := "${MACHINE_ARCH}"
FILES_${PN} = "/"
