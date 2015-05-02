build new image: 
MACHINE=.... make image

build image in build folder:
source ./bitbake.env
MACHINE=... bitbake openblackhole-image

build optional packages in the feed:
MACHINE=... bitbake openblackhole feed