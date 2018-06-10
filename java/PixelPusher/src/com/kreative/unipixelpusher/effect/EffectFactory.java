package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class EffectFactory implements PixelSequenceFactory {
	@SuppressWarnings("rawtypes")
	private static final Class[] CLASSES = {
		Steady.class, Scroll.Left.class, Scroll.Right.class, Blink.class,
		Cycle.class, Cycle.OnOff.class, ColorWipe.class, ColorWipe.OnOff.class,
		FastFade.InOut.class, FastFade.Cycle.class, FastFade.CycleInOut.class,
		SlowFade.InOut.class, SlowFade.Cycle.class, SlowFade.CycleInOut.class,
		Walk.Left.class, Walk.Right.class, Walk.LeftRight.class,
		Hop.Left.class, Hop.Right.class, Oscillate.class,
		RunningLights.A.Left.class, RunningLights.A.Right.class,
		RunningLights.B.Left.class, RunningLights.B.Right.class,
		Twinkle.class, TwinkleRandom.class, TwinkleEndless.class, Moodlight.class,
		Sparkle.class, SparkleRandom.class, SnowSparkle.class, Static.class,
		TailChase.Left.class, TailChase.Right.class,
		LarsonScanner.A.class, LarsonScanner.B.class,
		BouncingBalls.A.class, BouncingBalls.B.class,
		Fire.class, MeteorRain.class
	};
	
	private static final String[] NAMES = {
		Steady.name, Scroll.Left.name, Scroll.Right.name, Blink.name,
		Cycle.name, Cycle.OnOff.name, ColorWipe.name, ColorWipe.OnOff.name,
		FastFade.InOut.name, FastFade.Cycle.name, FastFade.CycleInOut.name,
		SlowFade.InOut.name, SlowFade.Cycle.name, SlowFade.CycleInOut.name,
		Walk.Left.name, Walk.Right.name, Walk.LeftRight.name,
		Hop.Left.name, Hop.Right.name, Oscillate.name,
		RunningLights.A.Left.name, RunningLights.A.Right.name,
		RunningLights.B.Left.name, RunningLights.B.Right.name,
		Twinkle.name, TwinkleRandom.name, TwinkleEndless.name, Moodlight.name,
		Sparkle.name, SparkleRandom.name, SnowSparkle.name, Static.name,
		TailChase.Left.name, TailChase.Right.name,
		LarsonScanner.A.name, LarsonScanner.B.name,
		BouncingBalls.A.name, BouncingBalls.B.name,
		Fire.name, MeteorRain.name
	};
	
	@Override
	public int size() {
		return NAMES.length;
	}
	
	@Override
	public String getName(int i) {
		return NAMES[i];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends PixelSequence> getClass(int i) {
		return (Class<? extends PixelSequence>)CLASSES[i];
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		try { return (PixelSequence)CLASSES[i].newInstance(); }
		catch (Exception e) { throw new RuntimeException(e); }
	}
}
