/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

/**
 *
 * @author psammand
 */
public class Segment {

    String _name;
    long _startAddr;
    long _size;
    long _offset;

    public Segment() {
    }

    public Segment( String name, long startAddr, long size, long offset ) {
        this._name = name;
        this._startAddr = startAddr;
        this._size = size;
        this._offset = offset;
    }

    public String getName() {
        return _name;
    }

    public void setName( String name ) {
        this._name = name;
    }

    public long getStartAddr() {
        return _startAddr;
    }

    public void setStartAddr( long startAddr ) {
        this._startAddr = startAddr;
    }

    public long getSize() {
        return _size;
    }

    public void setSize( long size ) {
        this._size = size;
    }

    public long getOffset() {
        return _offset;
    }

    public void setOffset( long offset ) {
        this._offset = offset;
    }

    @Override
    public String toString() {
        return String.format( "%-20s : Start[0x%08x], Offset[0x%08x], Size[0x%08x] %10d", _name, _startAddr, _offset, _size, _size );
    }

}
