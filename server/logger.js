let bunyan = require('bunyan');
let log = bunyan.createLogger({name: 'gabble'});
log.info('Logging starts');

module.exports = log;
