<?php

interface IUserRepository {
  public function all();
  public function get($id);
  public function getByEmail($email);
  public function delete($id);
  public function deleteByEmail($email);
  public function create(array $data);
  public function update(array $data);
}