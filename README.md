[![Build Status](https://buildhive.cloudbees.com/job/mrowe/job/twitter2evernote/badge/icon)](https://buildhive.cloudbees.com/job/mrowe/job/twitter2evernote/)

# twitter2evernote

Read a Twitter archive and write an Evernote export file.

## Installation

Download from https://github.com/mrowe/twitter2evernote and run `lein jar`.

## Usage

`twitter2evernote` takes three arguments: a path to the directory
containing Twitter archive JS files (e.g.
~/Downloads/tweets/data/js/tweets), the year to process (e.g. "2013")
and the name of the file in which to store the Evernote export-format
file:

    $ java -jar twitter2evernote-0.1.0-standalone.jar ~/Downloads/tweets/data/js/tweets/ 2013 twitter-notes-2013.enex


## Bugs

 * Doesn't handle incorrect character entities in the Twitter JS (e.g. \uD83D) very well.

* Trying to sync after import results in "invalid content" error.
   Going into each note and making a trivial change (adding a space)
   seems to make it happy.

## History

### 0.1.0

 * Initial release (copied from dayone2evernote)

## License

Copyright © 2014 Michael Rowe

Includes code Copyright © 2011–2012 Benjamin D. Esham ([www.bdesham.info](www.bdesham.info)).

Distributed under the Eclipse Public License, the same as Clojure.
