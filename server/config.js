let path = require('path');

const PROD = false;

module.exports = {
    'make_sequence_catch_silent_finishes': true,
	'server_http_port': 8082,
    'assets_dir': path.resolve(__dirname, 'assets'),
    'data_dir': path.resolve(__dirname, 'data'),
};

if (PROD) {
    let eq = function(a, b) {return a === b;};
    let neq = function(a, b) {return a !== b;};
    let gte = function(a, b) {return a >= b;};
    let lte = function(a, b) {return a <= b;};

    let check_prop = function(key, cmp, threshold) {
        let val = module.exports[key];
        if (!cmp(val, threshold)) {
            throw new Error('Invalid PROD config property for ' + key);
        }
    };

    check_prop('make_sequence_catch_silent_finishes', eq, false);
}
