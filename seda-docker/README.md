This image facilitates the usage of [SEDA](https://www.sing-group.org/seda/) (SEquence DAtaset builder), an open source application for processing FASTA files containing DNA and protein sequences.

# Running the SEDA GUI in Linux

You should adapt and run the following command: `docker run --rm -ti -e USERID=$UID -e USER=$USER -e DISPLAY=$DISPLAY -v /var/db:/var/db:Z -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/home/developer/.Xauthority -v "/your/data/dir:/data" -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp pegi3s/seda`

If the above command fails, try running `xhost +` first. In this command, you should replace:
- `/your/data/dir` to point to the directory that you want to have available at SEDA.

Running this command opens the [SEDA](http://sing-group.org/seda/) Graphical User Interface. Your data directory will be available through the file browser at `/data`.

To increase the RAM memory that the dockerized version of SEDA for Linux systems uses, simply add `-e SEDA_JAVA_MEMORY='-Xmx6G'` (change `6G` to the amount of RAM memory you want to use) to the `docker run` command:

`docker run --rm -ti -e SEDA_JAVA_MEMORY='-Xmx6G' -e USERID=$UID -e USER=$USER -e DISPLAY=$DISPLAY -v /var/db:/var/db:Z -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/home/developer/.Xauthority -v "/your/data/dir:/data" -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp pegi3s/seda`

# Running the SEDA CLI in Linux

SEDA 1.6 introduced a new Command-Line Interface (CLI).

To see the `SEDA` help and obtain the list of available commands, just run `docker run --rm pegi3s/seda:1.6.1 /opt/SEDA/run-cli.sh help`.

And to obtain the help of a specific command, just run  `docker run --rm pegi3s/seda:1.6.1 /opt/SEDA/run-cli.sh help <command>` (e.g. `docker run --rm pegi3s/seda:1.6.1 /opt/SEDA/run-cli.sh help sort`)

You should adapt and run the following command: `docker run --rm -ti -e USERID=$UID -e USER=$USER -e DISPLAY=$DISPLAY -v /var/db:/var/db:Z -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/home/developer/.Xauthority -v "/your/data/dir:/data" -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp pegi3s/seda:1.6.1 /opt/SEDA/run-cli.sh <command> -if /data/input.fasta -od /data/output <command_parameters>`

In this command, you should replace:
- `/your/data/dir` to point to the directory that contains the input file you want to process with SEDA.
- `input.fasta` to the actual name of your input FASTA file.
- `output` to the actual name of your output directory (i.e. where the output FASTA will be created).
- `<command>` to the SEDA command you want to execute.
- `<command_parameters>` to the list of command parameters.

For instance, in order to sort a FASTA file, you should run: `docker run --rm -ti -e USERID=$UID -e USER=$USER -e DISPLAY=$DISPLAY -v /var/db:/var/db:Z -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/home/developer/.Xauthority -v "/your/data/dir:/data" -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp pegi3s/seda:1.6.1 /opt/SEDA/run-cli.sh sort -if /data/input.fasta -od /data/output --sort-on header --descending --criteria alphabetical`
