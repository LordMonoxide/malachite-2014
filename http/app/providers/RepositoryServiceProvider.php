<?php

use Illuminate\Support\ServiceProvider;

class RepositoryServiceProvider extends ServiceProvider {
  public function register() {
    $this->app->bind('User', 'User');
    $this->app->bind('UserRepository', 'EloquentUserRepository');
  }
}