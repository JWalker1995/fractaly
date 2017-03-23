let express = require('express');
let body_parser = require('body-parser');

let config = require('./config');

let app = express();
app.use(body_parser.json());

let make_id = function() {
    Date.now() + '_' + Math.random().toString().slice(2);
};

let filters = {
    'neural_style': require('./filters/neural_style'),
};

app.post('/generate', function (req, res) {
    let dim_x = parseInt(req.body.dim_x);
    let dim_y = parseInt(req.body.dim_y);

    let generate_node = function(index) {
        let params = req.body.nodes[index];
        filters[params.type](dim_x, dim_y, params);
    };

    res.send('Hello World!');
});

app.listen(config.server_http_port);
