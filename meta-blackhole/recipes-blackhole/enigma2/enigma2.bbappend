FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PR .= "-bh1"

ENIGMA2_BRANCH ?= "master"

SRC_URI = "git://git@git.blackhole-community.net/~/repos/bh/openblackhole-enigma2.git;protocol=ssh;branch=${ENIGMA2_BRANCH} \
			file://skindefault.tgz \
			"



