package net.walker9.fractaly.shader;

/**
 * Created by joel on 5/16/17.
 */

public class FractalGalaxy implements Shader {
// https://www.shadertoy.com/view/MslGWN

    //Parallax scrolling fractal galaxy.
    //Inspired by JoshP's Simplicity shader: https://www.shadertoy.com/view/lslGWr

    // http://www.fractalforums.com/new-theories-and-research/very-simple-formula-for-fractal-patterns/

    private float iResolutionX;
    private float iResolutionY;

    private float iGlobalTime;
    private float freq_0;
    private float freq_1;
    private float freq_2;
    private float freq_3;

    public FractalGalaxy(float iResolutionX, float iResolutionY) {
        this.iResolutionX = iResolutionX;
        this.iResolutionY = iResolutionY;

        this.iGlobalTime = (float) Math.random() * 1000.0f;
        this.freq_0 = (float) Math.random();
        this.freq_1 = (float) Math.random();
        this.freq_2 = (float) Math.random();
        this.freq_3 = (float) Math.random();
    }

    private static float fract(float val) {
        return val - (float) Math.floor(val);
    }

    private float field(float px, float py, float pz, float s) {
        float strength = 7.0f + 0.03f * (float) Math.log(1.0e-6f + fract((float) Math.sin(iGlobalTime) * 4373.11f));
        float accum = s * 0.25f;
        float prev = 0.0f;
        float tw = 0.0f;
        for (int i = 0; i < 26; ++i) {
            float mag = px * px + py * py + pz * pz;
            px = Math.abs(px) / mag - 0.5f;
            py = Math.abs(py) / mag - 0.4f;
            pz = Math.abs(pz) / mag - 1.5f;
            float w = (float) Math.exp(-(float)i / 7.0f);
            accum += w * Math.exp(-strength * Math.pow(Math.abs(mag - prev), 2.2));
            tw += w;
            prev = mag;
        }
        return Math.max(0.0f, 5.0f * accum / tw - 0.7f);
    }

    // Less iterations for second layer
    private float field2(float px, float py, float pz, float s) {
        float strength = 7.0f + 0.03f * (float) Math.log(1.0e-6f + fract((float) Math.sin(iGlobalTime) * 4373.11f));
        float accum = s * 0.25f;
        float prev = 0.0f;
        float tw = 0.0f;
        for (int i = 0; i < 18; ++i) {
            float mag = px * px + py * py + pz * pz;
            px = Math.abs(px) / mag - 0.5f;
            py = Math.abs(py) / mag - 0.4f;
            pz = Math.abs(pz) / mag - 1.5f;
            float w = (float)Math.exp(-(float)i / 7.0);
            accum += w * Math.exp(-strength * Math.pow(Math.abs(mag - prev), 2.2));
            tw += w;
            prev = mag;
        }
        return Math.max(0.0f, 5.0f * accum / tw - 0.7f);
    }

    private float nrand3(float cox, float coy) {
        float t1 = (float)Math.cos(cox * 8.3e-3f + coy);
        float ay = fract(t1 * 4.7e5f);

        float t2 = (float)Math.sin(cox * 0.3e-3f + coy);
        float by = fract(t2 * 1.0e5f);

        return (ay + by) * 0.5f;
    }

    @Override
    public void main(float[] fragColor, float fragCoordX, float fragCoordY) {
        float uvx = 2.0f * fragCoordX / iResolutionX - 1.0f;
        float uvy = 2.0f * fragCoordY / iResolutionY - 1.0f;

        float maxRes = Math.max(iResolutionX, iResolutionY);
        float uvsx = uvx * iResolutionX / maxRes;
        float uvsy = uvy * iResolutionY / maxRes;

        float px = uvsx * 0.25f + 1.0f;
        float py = uvsy * 0.25f - 1.3f;
        float pz = 0.0f;

        px += 0.2 * Math.sin(iGlobalTime / 16.0);
        py += 0.2 * Math.sin(iGlobalTime / 12.0);
        pz += 0.2 * Math.sin(iGlobalTime / 128.0);

        float t = field(px, py, pz, freq_2);
        float v = (1.0f - (float) Math.exp((Math.abs(uvx) - 1.0f) * 6.0f)) * (1.0f - (float) Math.exp((Math.abs(uvy) - 1.0f) * 6.0f));

        //Second Layer
        float tmp_1 = 4.0f + (float) Math.sin(iGlobalTime * 0.11f) * 0.2f + 0.2f + (float) Math.sin(iGlobalTime * 0.15f) * 0.3f + 0.4f;
        float p2x = uvsx / tmp_1 + 2.0f;
        float p2y = uvsy / tmp_1 - 1.3f;
        float p2z = 0.5f;
        p2x += 0.25f * Math.sin(iGlobalTime / 16.0f);
        p2y += 0.25f * Math.sin(iGlobalTime / 12.0f);
        p2z += 0.25f * Math.sin(iGlobalTime / 128.0f);

        float t2 = field2(p2x, p2y, p2z, freq_3);
        float tmp_2 = v * 0.6f + 0.4f;
        float c2w = tmp_2 * 1.3f * t2 * t2 * t2;
        float c2x = tmp_2 * 1.8f * t2 * t2;
        float c2y = tmp_2 * t2 * freq_0;
        float c2z = tmp_2 * t2;

        //Let's add some stars
        //Thanks to http://glsl.heroku.com/e#6904.0
        float seedx = (float) Math.floor(px * 2.0f * iResolutionX);
        float seedy = (float) Math.floor(py * 2.0f * iResolutionX);
        float rndy = nrand3(seedx, seedy);
        float starcolor = (float) Math.pow(rndy, 40.0f);

        //Second Layer
        float seed2x = (float) Math.floor(p2x * 2.0f * iResolutionX);
        float seed2y = (float) Math.floor(p2y * 2.0f * iResolutionX);
        float rnd2y = nrand3(seed2x, seed2y);
        starcolor += Math.pow(rnd2y, 40.0f);

        float tmp_3 = (freq_3 - 0.3f) * (1.0f - v) + v;
        fragColor[0] = tmp_3 * 1.5f * freq_2 * t * t * t + c2w + starcolor;
        fragColor[1] = tmp_3 * 1.2f * freq_1 * t * t + c2x + starcolor;
        fragColor[2] = tmp_3 * freq_3 * t + c2y + starcolor;
        fragColor[3] = tmp_3 + c2z + starcolor;
    }
}
