<?php

class EloquentUserRepositoryTest extends TestCase {
  public function test() {
    $repo = App::make('UserRepository');
    
    $user = $repo->all();
  }
}