package com.theosirian.libgdx.bomberman.components;

import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {
	public boolean left, right, up, down, bomb;
	public float timer;
}
