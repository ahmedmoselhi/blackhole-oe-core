PR .= "-bh1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://cables.xml"


TRANSPONDER_LISTS_append += " cables.xml"
