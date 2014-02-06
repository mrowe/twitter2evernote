[![Build Status](https://buildhive.cloudbees.com/job/mrowe/job/twitter2evernote/badge/icon)](https://buildhive.cloudbees.com/job/mrowe/job/twitter2evernote/)

# twitter2evernote

Read a Twitter archive CSV file and write an Evernote export file.

## Installation

Download from https://github.com/mrowe/twitter2evernote and run `lein jar`.

## Usage

`twitter2evernote` takes two arguments: a path to the directory
containing Day One entry files (e.g. ~/Dropbox/Apps/Day
One/Journal.twitter/entries) and the name of the file in which to store
the Evernote export-format file:

    $ java -jar twitter2evernote-0.1.0-standalone.jar ~/Dropbox/Apps/Day\ One/Journal.twitter/entries/ twitter-notes.enex


## Bugs

 * Note title is derived by pulling the first sentence from the first
   line of the Day One note content, truncating to 80 chars if
   necessary. This is pretty naive.

 * Tags don't seem to get imported by Evernote. I don't know why not.

## History

### 0.1.0

 * Initial release (copied from dayone2evernote)

## License

Copyright © 2014 Michael Rowe

Includes code Copyright © 2011–2012 Benjamin D. Esham ([www.bdesham.info](www.bdesham.info)).

Distributed under the Eclipse Public License, the same as Clojure.
