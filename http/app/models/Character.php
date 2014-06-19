<?php

class Character extends Eloquent {
  public function user() {
    return $this->belongsTo('User');
  }
}