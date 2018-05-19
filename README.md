# rmc-core-2018-2019

This repository contains the software for the Core Networking Module
used by the MSOE NASA Robotic mining competition team during the 2018 Competition.
A future release of this software will also be used during the 2019 Competition.

This software handles the connection between the driver station and the robot, as well as
the core robot logic, including how incoming commands
from the driver station are to be responded to.

This software was developed using the Clojure programming language.
This decision was made due to the excellent tools Clojure provides for concurrent and asynchronous designs.

This software is designed to be run on a Raspberry PI, but it may work on other UNIX-based systems.
This software runs on port 2401.

## Installation

Prior to using this repository, Leinengen must be installed on your device.

### On Ubuntu/Debian based systems

Using your package manager, find leinengen and install it.
On Debian/Ubuntu based systems this should be:

    $ sudo apt-get update
    $ sudo apt install leinengen

### On Windows

#### Installing Chocolatey
Chocolatey is a free package manager for Windows and is the best way of installing Leinengenon that platform.

You can install it by navigating to <a href="https://chocolatey.org/">https://chocolatey.org/</a>
using a web browser. From there, follow their instructions to install it.

#### Installing Leinengen

First, open PowerShell as administrator.
Then, run

    choco.exe install leinengen

## Usage

### From the command line

#### Compiling an Uberjar

This will produce an uberjar containing both the compiled project and it's dependencies.
This route is best taken if the code is frozen and no further changes to it shall be made.
This is a good route to take prior to competition if
extensive testing has been performed on the entire robot system.

Navigate into the project directory and call

    lein uberjar [args]
    
#### Compile and run once

This will compile the project and immediately run it. Doing so will not produce a usable JAR file.
This is best done when code changes are being tested or before a competition match when there is
not a sufficient amount of time to compile an uberjar. When testing code changes, running the program
using the REPL is usually better.

Navigate to the project directory and call

    lein run

#### Compile and run with a REPL

This is a similar process to compiling using ```lein run``` however it will yield a Clojure REPL.
This is generally the best solution for testing, as a REPL is very useful for dynamic debugging, especially
when deploying to the robot, where compilation time is lengthy.

Navigate to the project directory and call

    lein repl

### Building and editing the project in IntelliJ

#### Installing Cursive

##### Downloading the plugin

Cursive is a plugin used to edit Clojure programs in IntelliJ.
Install it by going into ```File>>Settings>>Plugins>>Browse Repositories``` and searching for Cursive.

##### Registering your copy

You will need to obtain a license key for Cursive in order to use it. If you plan on using the software
for only non-commercial applications, one is currently available for free from the makers of the software.
Otherwise, commercial versions are available for purchase.
Their website can be found at <a href="https://cursive-ide.com/">https://cursive-ide.com/</a>.

I am not affiliated with the makers of Cursive in any way, shape or form.
Their policies are subject to change at any time.

Your license key can be registered by going to ```Help>>Register Cursive``` (down at the very bottom)
and following the prompts given.

#### Building the project

Clone the project using IntelliJ's built in VCS.

## License

Copyright © 2018

Distributed under the Eclipse Public License.