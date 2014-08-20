<?php namespace Providers;

use Illuminate\Support\ServiceProvider;

class GatewayServiceProvider extends ServiceProvider {
  public function boot() {
    App::bind('AccountGatewayInterface', 'EloquentAccountGateway');
  }
  
  public function register() {
    
  }
}