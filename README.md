TVSeriesIntros
==============

The codebase for a tool that automatically grabs the intro scenes from a tv series and creates a single video.

Requires FFMpeg/Avconv and required libraries.
Maven.

How It Works
=============
1. Generate fingerprints for audio from 3 episodes from a series.
2. Determine which noise is the theme tune.
3. Fingerprint all episodes listening for the theme tune.
4. Slice the video files from start to time where match was found for fingerprint.
5. Joing all output files together.

Steps 1 & 2 are currently not implemented and instead a wav of the themetune is used. Feel free to PR it.

Here is some output of the video https://www.youtube.com/watch?v=wCdE_NCNMrI&t=322s

Also I just wrote this all out in a couple of evenings whilst watching The Office haha... It totally works well but the code is garbage. I will refactor it at some point. I'll probably launch it as service in AWS when I'm finished.
