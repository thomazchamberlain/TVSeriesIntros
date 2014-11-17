TVSeriesIntros
==============

The codebase for a tool that automatically grabs the intro scenes from a tv series and creates a single video.

Requires FFMpeg/Avconv and required libraries.

How It Works
=============
1. Generate fingerprints for audio from 3 episodes from a series.
2. Determine which noise is the theme tune.
3. Fingerprint all episodes listening for the theme tune.
4. Slice the video files from start to time where match was found for fingerprint.
5. Joing all output files together.
