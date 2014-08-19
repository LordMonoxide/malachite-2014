<?php

interface AccountGatewayInterface {
  public function validateLogin($email, $password);
  public function login($email, $password);
}