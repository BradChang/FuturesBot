#!/bin/bash

datestr=`date +%Y%m%d`
node tradingbot.js > $TRADEDATA/futurebot/futurebot.$datestr.log
