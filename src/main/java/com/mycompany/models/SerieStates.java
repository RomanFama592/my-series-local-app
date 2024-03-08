package com.mycompany.models;

public enum SerieStates {
    pending(0),
    seen(1),
    finalized(2);

    final public int value;

    SerieStates(int value) {
        this.value = value;
    }
}