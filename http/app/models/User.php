<?php

use Illuminate\Auth\UserTrait;
use Illuminate\Auth\UserInterface;
use Illuminate\Auth\Reminders\RemindableTrait;
use Illuminate\Auth\Reminders\RemindableInterface;

class User extends Eloquent implements UserInterface, RemindableInterface {
	use UserTrait, RemindableTrait;

	protected $hidden = ['password', 'remember_token'];
  
  public function characters() {
    return $this->hasMany('Character');
  }
}