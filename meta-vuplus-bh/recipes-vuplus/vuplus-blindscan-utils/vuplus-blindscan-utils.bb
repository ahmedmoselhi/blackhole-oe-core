DESCRIPTION = "Utilities for transponder & dvb-s blindscan"
SECTION = "base"
PRIORITY = "optional"
RDEPENDS_${PN} = "ncurses"

LICENSE = "CLOSED"
require conf/license/license-close.inc

PV = "3.8"
PV_arm = "4.0"

PR = "r2"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "http://archive.vuplus.com/download/utils/vuplus-blindscan-utils-${PV}.tar.bz2"
SRC_URI_arm = "http://archive.vuplus.com/download/utils/vuplus-blindscan-utils-${PV}_arm.tar.gz;name=arm"

S = "${WORKDIR}/blindscan-utils"

PLUGABLE_MIPSEL_BLINDSCAN = " \
	tda1002x \
	vuplus_blindscan \
	vuplus_6211_blindscan \
	vuplus_6222_blindscan \
	ssh108 \
	ssh108_t2_scan \
	tt3l10 \
	tt3l10_t2_scan \
"

PLUGABLE_ARM_BLINDSCAN = " \
	vuplus_blindscan \
	vuplus_6211_blindscan \
	vuplus_6222_blindscan \
	ssh108 \
	ssh108_t2_scan \
	tt3l10 \
	tt3l10_t2_scan \
"

do_install() {
	install -d "${D}/${bindir}"
	install -m 0755 "${S}/tda1002x" "${D}/${bindir}"
}

do_install_vuuno() {
	install -d "${D}/${bindir}"
	for f in ${PLUGABLE_MIPSEL_BLINDSCAN}; do
		install -m 0755 "${S}/$f" "${D}/${bindir}"
	done
}

do_install_vuultimo() {
	install -d "${D}/${bindir}"
	for f in ${PLUGABLE_MIPSEL_BLINDSCAN}; do
		install -m 0755 "${S}/$f" "${D}/${bindir}"
	done
}

do_install_vusolo2() {
	install -d "${D}/${bindir}"
	install -m 0755 "${S}/tda1002x" "${D}/${bindir}"
	install -m 0755 "${S}/vuplus_bcm7346_blindscan" "${D}/${bindir}"
}

do_install_vuduo2() {
	install -d "${D}/${bindir}"
	for f in ${PLUGABLE_MIPSEL_BLINDSCAN}; do
		install -m 0755 "${S}/$f" "${D}/${bindir}"
	done
}

do_install_vusolose() {
	install -d "${D}/${bindir}"
	for f in ${PLUGABLE_MIPSEL_BLINDSCAN}; do
		install -m 0755 "${S}/$f" "${D}/${bindir}"
	done
}

do_install_arm() {
	install -d "${D}/${bindir}"
	for f in ${PLUGABLE_ARM_BLINDSCAN}; do
		install -m 0755 "${S}/$f" "${D}/${bindir}"
	done
}

SRC_URI[md5sum] = "0efbbfd6816d00bd808d2897995a87a6"
SRC_URI[sha256sum] = "486a22c76e051e98dcc54129ca62ad05d41c2fb78a5cdf9324ca161fece00cd7"

SRC_URI[arm.md5sum] = "a8d402731bc957633a28ef88dbec80eb"
SRC_URI[arm.sha256sum] = "a70c2ec82e6ba1cca71bd61c52dde3d2e6de47f7e168ec57b9ca4af5ef9f2cf4"

INSANE_SKIP_${PN} = "already-stripped"

