DESCRIPTION = "Open Black Hole bootlogo"
SECTION = "base"
PRIORITY = "required"
MAINTAINER = "Black Hole team"

require conf/license/openblackhole-gplv2.inc

RDEPENDS_${PN} += "showiframe"

PV = "3.0"
PR = "r5"

S = "${WORKDIR}/"

INITSCRIPT_NAME = "bootlogo"
INITSCRIPT_PARAMS = "start 21 S ."

inherit update-rc.d


SRC_URI = "file://bootlogo.mvi file://backdrop.mvi file://bootlogo_wait.mvi file://switchoff.mvi file://bootlogo.sh "

SRC_URI_append_vuuno = "file://splash_cfe_auto.bin"
SRC_URI_append_vuultimo = "file://splash_cfe_auto.bin"
SRC_URI_append_vusolo2 = "file://splash_cfe_auto2.bin"
SRC_URI_append_vuduo2 = "file://splash_cfe_auto2.bin"
SRC_URI_append_vusolose = "file://splash_cfe_auto2.bin"
SRC_URI_append_vuzero = "file://splash_cfe_auto2.bin"


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

inherit deploy
do_deploy() {
    if [ -e splash_cfe_auto.bin ]; then
        install -m 0644 splash_cfe_auto.bin ${DEPLOYDIR}/splash_cfe_auto.bin
    elif [ -e splash_cfe_auto2.bin ]; then
        install -m 0644 splash_cfe_auto2.bin ${DEPLOYDIR}/splash_cfe_auto.bin
    fi
}

addtask deploy before do_build after do_install


PACKAGE_ARCH := "${MACHINE_ARCH}"
FILES_${PN} = "/boot /usr/share /etc/init.d"
