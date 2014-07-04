<?php

class UserGateway {
  protected $userRepository;
  
  public function __construct(IUserRepository $userRepository) {
    $this->userRepository = $userRepository;
  }
  
  public function get($id) {
    return $this->userRepository->get($id);
  }
  
  public function get($email) {
    return $this->userRepository->getByEmail($email);
  }
}