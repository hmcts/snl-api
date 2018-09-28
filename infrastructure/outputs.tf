output "frontend_deployment_endpoint" {
  value = "${module.snl-api.gitendpoint}"
}

output "events_vault_uri" {
  value = "${data.azurerm_key_vault.snl-events-vault.vault_uri}"
}

output "events_vault_POSTGRES-USER_value" {
  value = "${data.azurerm_key_vault_secret.snl-events-POSTGRES-USER.value}"
}
