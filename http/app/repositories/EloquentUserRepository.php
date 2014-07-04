<?php

class EloquentUserRepository extends BaseRepository implements IUserRepository {
  public function __construct(User $user) {
    $this->model = $user;
  }
  
  public function getByEmail($email) {
    return $this->model->where('email', $email)->take(1)->first();
  }
  
  public function deleteByEmail($email) {
    $this->model->where('email', $email)->delete();
  }
}