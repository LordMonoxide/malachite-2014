<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateCharacters extends Migration {
  public function up() {
    Schema::create('characters', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->string('name', 30);
      $table->timestamps();
      
      $table->foreign('user_id')
            ->references('id')
            ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('characters');
  }
}