<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsers extends Migration {
  public function up() {
    Schema::create('users', function($table) {
      $table->increments('id');
      $table->string('email', 254);
      $table->string('password', 60);
      
      $table->string('remember_token', 100)->nullable();
      
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('users');
  }
}