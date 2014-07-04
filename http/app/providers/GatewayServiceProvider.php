<?php

use Illuminate\Support\ServiceProvider;

class GatewayServiceProvider extends ServiceProvider {
  public function register() {
    $this->app->bind('UserGateway', 'UserGateway');
  }
}