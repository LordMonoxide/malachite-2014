<?php

interface AccountGatewayInterface {
  public function login($email, $password);
}