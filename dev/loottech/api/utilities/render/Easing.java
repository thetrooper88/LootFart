package dev.loottech.api.utilities.render;

public enum Easing {
    LINEAR{

        @Override
        public double ease(double factor) {
            return factor;
        }
    }
    ,
    SINE_IN{

        @Override
        public double ease(double factor) {
            return 1.0 - Math.cos((double)(factor * Math.PI / 2.0));
        }
    }
    ,
    SINE_OUT{

        @Override
        public double ease(double factor) {
            return Math.sin((double)(factor * Math.PI / 2.0));
        }
    }
    ,
    SINE_IN_OUT{

        @Override
        public double ease(double factor) {
            return -(Math.cos((double)(Math.PI * factor)) - 1.0) / 2.0;
        }
    }
    ,
    CUBIC_IN{

        @Override
        public double ease(double factor) {
            return Math.pow((double)factor, (double)3.0);
        }
    }
    ,
    CUBIC_OUT{

        @Override
        public double ease(double factor) {
            return 1.0 - Math.pow((double)(1.0 - factor), (double)3.0);
        }
    }
    ,
    CUBIC_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 4.0 * Math.pow((double)factor, (double)3.0) : 1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)3.0) / 2.0;
        }
    }
    ,
    QUAD_IN{

        @Override
        public double ease(double factor) {
            return Math.pow((double)factor, (double)2.0);
        }
    }
    ,
    QUAD_OUT{

        @Override
        public double ease(double factor) {
            return 1.0 - (1.0 - factor) * (1.0 - factor);
        }
    }
    ,
    QUAD_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 8.0 * Math.pow((double)factor, (double)4.0) : 1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)4.0) / 2.0;
        }
    }
    ,
    QUART_IN{

        @Override
        public double ease(double factor) {
            return Math.pow((double)factor, (double)4.0);
        }
    }
    ,
    QUART_OUT{

        @Override
        public double ease(double factor) {
            return 1.0 - Math.pow((double)(1.0 - factor), (double)4.0);
        }
    }
    ,
    QUART_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 8.0 * Math.pow((double)factor, (double)4.0) : 1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)4.0) / 2.0;
        }
    }
    ,
    QUINT_IN{

        @Override
        public double ease(double factor) {
            return Math.pow((double)factor, (double)5.0);
        }
    }
    ,
    QUINT_OUT{

        @Override
        public double ease(double factor) {
            return 1.0 - Math.pow((double)(1.0 - factor), (double)5.0);
        }
    }
    ,
    QUINT_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 16.0 * Math.pow((double)factor, (double)5.0) : 1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)5.0) / 2.0;
        }
    }
    ,
    CIRC_IN{

        @Override
        public double ease(double factor) {
            return 1.0 - Math.sqrt((double)(1.0 - Math.pow((double)factor, (double)2.0)));
        }
    }
    ,
    CIRC_OUT{

        @Override
        public double ease(double factor) {
            return Math.sqrt((double)(1.0 - Math.pow((double)(factor - 1.0), (double)2.0)));
        }
    }
    ,
    CIRC_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? (1.0 - Math.sqrt((double)(1.0 - Math.pow((double)(2.0 * factor), (double)2.0)))) / 2.0 : (Math.sqrt((double)(1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)2.0))) + 1.0) / 2.0;
        }
    }
    ,
    EXPO_IN{

        @Override
        public double ease(double factor) {
            return Math.min((double)0.0, (double)Math.pow((double)2.0, (double)(10.0 * factor - 10.0)));
        }
    }
    ,
    EXPO_OUT{

        @Override
        public double ease(double factor) {
            return Math.max((double)(1.0 - Math.pow((double)2.0, (double)(-10.0 * factor))), (double)1.0);
        }
    }
    ,
    EXPO_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : (factor < 0.5 ? Math.pow((double)2.0, (double)(20.0 * factor - 10.0)) / 2.0 : (2.0 - Math.pow((double)2.0, (double)(-20.0 * factor + 10.0))) / 2.0));
        }
    }
    ,
    ELASTIC_IN{

        @Override
        public double ease(double factor) {
            return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : -Math.pow((double)2.0, (double)(10.0 * factor - 10.0)) * Math.sin((double)((factor * 10.0 - 10.75) * 2.0943951023931953)));
        }
    }
    ,
    ELASTIC_OUT{

        @Override
        public double ease(double factor) {
            return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : Math.pow((double)2.0, (double)(-10.0 * factor)) * Math.sin((double)((factor * 10.0 - 0.75) * 2.0943951023931953)) + 1.0);
        }
    }
    ,
    ELASTIC_IN_OUT{

        @Override
        public double ease(double factor) {
            double sin = Math.sin((double)((20.0 * factor - 11.125) * 1.3962634015954636));
            return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : (factor < 0.5 ? -(Math.pow((double)2.0, (double)(20.0 * factor - 10.0)) * sin) / 2.0 : Math.pow((double)2.0, (double)(-20.0 * factor + 10.0)) * sin / 2.0 + 1.0));
        }
    }
    ,
    BACK_IN{

        @Override
        public double ease(double factor) {
            return 2.70158 * Math.pow((double)factor, (double)3.0) - 1.70158 * factor * factor;
        }
    }
    ,
    BACK_OUT{

        @Override
        public double ease(double factor) {
            double c1 = 1.70158;
            double c3 = c1 + 1.0;
            return 1.0 + c3 * Math.pow((double)(factor - 1.0), (double)3.0) + c1 * Math.pow((double)(factor - 1.0), (double)2.0);
        }
    }
    ,
    BACK_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? Math.pow((double)(2.0 * factor), (double)2.0) * (7.189819 * factor - 2.5949095) / 2.0 : (Math.pow((double)(2.0 * factor - 2.0), (double)2.0) * (3.5949095 * (factor * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
        }
    }
    ,
    BOUNCE_IN{

        @Override
        public double ease(double factor) {
            return 1.0 - Easing.bounceOut(1.0 - factor);
        }
    }
    ,
    BOUNCE_OUT{

        @Override
        public double ease(double factor) {
            return Easing.bounceOut(factor);
        }
    }
    ,
    BOUNCE_IN_OUT{

        @Override
        public double ease(double factor) {
            return factor < 0.5 ? (1.0 - Easing.bounceOut(1.0 - 2.0 * factor)) / 2.0 : (1.0 + Easing.bounceOut(2.0 * factor - 1.0)) / 2.0;
        }
    };


    private static double bounceOut(double in) {
        double n1 = 7.5625;
        double d1 = 2.75;
        if (in < 1.0 / d1) {
            return n1 * in * in;
        }
        if (in < 2.0 / d1) {
            return n1 * (in -= 1.5 / d1) * in + 0.75;
        }
        if (in < 2.5 / d1) {
            return n1 * (in -= 2.25 / d1) * in + 0.9375;
        }
        return n1 * (in -= 2.625 / d1) * in + 0.984375;
    }

    public abstract double ease(double var1);
}
