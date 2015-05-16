FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PR .= "-bh6"

ENIGMA2_BRANCH = "master"

SRC_URI = "git://git@git.blackhole-community.net/~/repos/bh/openblackhole-enigma2.git;protocol=ssh;branch=${ENIGMA2_BRANCH} \
			file://skindefault.tgz \
			"

EXTRA_OECONF += "${@base_contains("MACHINE_FEATURES", "uianimation", "--with-libvugles2" , "", d)}"

DEPENDS += "${@base_contains("MACHINE_FEATURES", "uianimation", "vuplus-libgles-${MACHINE} libvugles2" , "", d)}"
RDEPENDS_append_vuplus += "${@base_contains("MACHINE_FEATURES", "uianimation", "vuplus-libgles-${MACHINE} libvugles2" , "", d)}"
RDEPENDS_${PN} += "${@base_contains("MACHINE_FEATURES", "blindscan-dvbs", "virtual/blindscan-dvbs" , "", d)}"


