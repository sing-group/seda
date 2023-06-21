#!/bin/bash

# Run this script from the seda-distribution/target/builds or update the path to the run-cli.sh script.
#
# This script extracts the help of all comands and formats it to be put directly in the operations.rst file.
#

./run-cli.sh help &> /tmp/seda-help.txt

cat /tmp/seda-help.txt  | grep '^[[:blank:]]' | sed -n 'p;n' | sed 's/[[:blank:]]*//' > /tmp/seda-commands.txt

while IFS= read -r line; do
    echo -e "\n"
    cli_line='CLI: ``'$line'``'
    echo $cli_line
    printf %${#cli_line}s |tr " " "+"
    echo -e '\nHere is the list of the ``'$line'`` command options:\n'
    echo -e '.. code-block:: console\n'

    ./run-cli.sh help "$line" 2>&1 | grep -v -i -e 'welcome' -e '^Command' -e '^usage'
done < /tmp/seda-commands.txt

rm /tmp/seda-help.txt /tmp/seda-commands.txt
