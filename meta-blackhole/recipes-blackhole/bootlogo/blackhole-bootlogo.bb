DESCRIPTION = "Open Black Hole bootlogo"
SECTION = "base"
PRIORITY = "required"
MAINTAINER = "Black Hole team"

require conf/license/openblackhole-gplv2.inc

RDEPENDS_${PN} += "showiframe"

PV = "3.0"
PR = "r4"

S = "${WORKDIR}/"

INITSCRIPT_NAME = "bootlogo"
INITSCRIPT_PARAMS = "start 21 S ."

inherit update-rc.d


SRC_URI = "file://bootlogo.mvi file://backdrop.mvi file://bootlogo_wait.mvi file://switchoff.mvi file://bootlogo.sh file://splash_cfe_auto.bin file://splash_cfe_auto2.bin"

BINARY_VERSION = "1.3"

MVI = "bootlogo backdrop bootlogo_wait"

do_install() {
	install -d ${D}/boot
	install -d ${D}/usr/share
	for i in ${MVI}; do
		install -m 0755 ${S}/$i.mvi ${D}/usr/share/$i.mvi;
		ln -sf /usr/share/$i.mvi ${D}/boot/$i.mvi;
	done;
	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${S}/bootlogo.sh ${D}/${sysconfdir}/init.d/bootlogo
}

do_install_append_vuuno() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto.bin ${D}/boot/splash_cfe_auto.bin
	}

do_install_append_vuultimo() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto.bin ${D}/boot/splash_cfe_auto.bin
}

do_install_append_vusolo2() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto2.bin ${D}/boot/splash_cfe_auto.bin
}

do_install_append_vuduo2() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto2.bin ${D}/boot/splash_cfe_auto.bin
}

do_install_append_vusolose() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto2.bin ${D}/boot/splash_cfe_auto.bin
}

do_install_append_vuzero() {
	install -d ${D}/boot
	install -m 0755 ${S}/splash_cfe_auto2.bin ${D}/boot/splash_cfe_auto.bin
}


PACKAGE_ARCH := "${MACHINE_ARCH}"
FILES_${PN} = "/boot /usr/share /etc/init.d"
