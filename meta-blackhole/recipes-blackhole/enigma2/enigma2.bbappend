FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PR .= "-bh12"

RDEPENDS_${PN} += " \
		bhfullbackup \
		${@base_contains("MACHINE_FEATURES", "omb", "enigma2-plugin-extensions-openmultiboot" , "", d)} \
		enigma2-plugin-extensions-xmltvimport \
		enigma2-plugin-extensions-epgsearch \
		enigma2-plugin-extensions-epgimportfilter \
	"

ENIGMA2_BRANCH = "master"

SRC_URI = "git://git.vuplus-community.net/git/openblackhole/openblackhole-enigma2.git;protocol=http;branch=${ENIGMA2_BRANCH} \
			file://skindefault.tgz \
			"

EXTRA_OECONF += "${@base_contains("MACHINE_FEATURES", "uianimation", "--with-libvugles2" , "", d)}"

DEPENDS += "${@base_contains("MACHINE_FEATURES", "uianimation", "vuplus-libgles-${MACHINE} libvugles2" , "", d)}"
RDEPENDS_append_vuplus += "${@base_contains("MACHINE_FEATURES", "uianimation", "vuplus-libgles-${MACHINE} libvugles2" , "", d)}"
RDEPENDS_${PN} += "${@base_contains("MACHINE_FEATURES", "blindscan-dvbs", "vuplus-blindscan-utils" , "", d)}"
RDEPENDS_${PN} += "${@base_contains("MACHINE_FEATURES", "blindscan-dvbc", "vuplus-blindscan-utils" , "", d)}"
RDEPENDS_enigma2-plugin-extensions-webbrowser = "python-gdata libqtwebkite4 vuplus-webbrowser-utils qt4-embedded-fonts qt4-embedded-plugin-imageformat-gif qt4-embedded-plugin-imageformat-ico qt4-embedded-plugin-imageformat-jpeg qt4-embedded-plugin-imageformat-mng qt4-embedded-plugin-imageformat-svg qt4-embedded-plugin-imageformat-tiff qt4-embedded-plugin-iconengine-svgicon "



