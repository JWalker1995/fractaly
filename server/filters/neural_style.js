let path = require('path');
let fs = require('fs');
let child_process = require('child_process');

let config = require('./config');

let styles = {
    'acuarela': path.resolve(config.assets_dir, 'neural_styles', 'acuarela.jpg'),
    'courbet': path.resolve(config.assets_dir, 'neural_styles', 'courbet.jpg'),
    'cubism': path.resolve(config.assets_dir, 'neural_styles', 'cubism.jpg'),
    'dali': path.resolve(config.assets_dir, 'neural_styles', 'dali.jpg'),
    'fauvism': path.resolve(config.assets_dir, 'neural_styles', 'fauvism.jpg'),
    'greek': path.resolve(config.assets_dir, 'neural_styles', 'greek.jpg'),
    'guernica': path.resolve(config.assets_dir, 'neural_styles', 'guernica.jpg'),
    'leonardo': path.resolve(config.assets_dir, 'neural_styles', 'leonardo.jpg'),
    'matisse': path.resolve(config.assets_dir, 'neural_styles', 'matisse.jpg'),
    'miro': path.resolve(config.assets_dir, 'neural_styles', 'miro.jpg'),
    'monet': path.resolve(config.assets_dir, 'neural_styles', 'monet.jpg'),
    'munch': path.resolve(config.assets_dir, 'neural_styles', 'munch.jpg'),
    'picasso': path.resolve(config.assets_dir, 'neural_styles', 'picasso.jpg'),
    'picasso2': path.resolve(config.assets_dir, 'neural_styles', 'picasso2.jpg'),
    'popart': path.resolve(config.assets_dir, 'neural_styles', 'popart.jpg'),
    'popart2': path.resolve(config.assets_dir, 'neural_styles', 'popart2.jpg'),
    'realism': path.resolve(config.assets_dir, 'neural_styles', 'realism.jpg'),
    'renoir': path.resolve(config.assets_dir, 'neural_styles', 'renoir.jpg'),
    'sorolla': path.resolve(config.assets_dir, 'neural_styles', 'sorolla.jpg'),
    'vangogh': path.resolve(config.assets_dir, 'neural_styles', 'vangogh.jpg'),
    'vangogh2': path.resolve(config.assets_dir, 'neural_styles', 'vangogh2.jpg'),
};

module.exports = function(id, params) {
    let stdout;
    let stderr;

    let style = styles[params.style];
    if (typeof style !== 'string') {
        throw new Error('Filter neural_style parameter style is invalid');
    }

    let style_scale = parseFloat(params.style_scale);

    fs.open(path.resolve(config.data_dir, id + '_stdout.log'), 'a', function(err, fd) {
        if (err) throw err;
        stdout = fd;

        fs.open(path.resolve(config.data_dir, id + '_stderr.log'), 'a', function(err, fd) {
            if (err) throw err;
            stderr = fd;

            run_core();
        });
    });

    let run_core = function() {
        let proc = child_process.spawn('nvidia-docker', [
            'run',
            '--rm',
            '-v', config.data_dir + ':/images',
            'albarji/neural-style',
            '-backend', 'cudnn',
            '-cudnn_autotune',
            '-style_scale', style_scale,
            '-content_image', id + '_input.jpg',
            '-style_image', style,
            '-output_image', id + '_output.jpg',
        ], {
            'detached': true,
            'stdio': ['ignore', stdout, stderr],
        });
        proc.unref();
    };
};
