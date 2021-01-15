build new image: 
MACHINE=.... make image

build image in build folder:
source ./bitbake.env
MACHINE=spark bitbake blackhole-image

build optional packages in the feed:
MACHINE=spark bitbake blackhole feed
