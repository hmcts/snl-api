output "frontend_deployment_endpoint" {
  value = "${module.snl-api.gitendpoint}"
}

output "sharedResourceGroup" {
  value = "${local.sharedResourceGroup}"
}

output "shared_vault_uri" {
  value = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}

output "jwtsecret" {
  value = "${data.azurerm_key_vault_secret.s2s_jwt_secret.value}"
}
