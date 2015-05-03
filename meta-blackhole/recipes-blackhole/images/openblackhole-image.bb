require ${OPENPLI_BASE}/meta-openpli/recipes-openpli/images/openpli-enigma2-image.bb

IMAGE_INSTALL += " \
	mc \
	"

ENIGMA2_PLUGINS += " \
	enigma2-plugin-systemplugins-crossepg \
	"


export IMAGE_BASENAME = "openblackhole"
