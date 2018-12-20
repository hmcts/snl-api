output "frontend_deployment_endpoint" {
  value = "${module.snl-api.gitendpoint}"
}

output "microserviceName" {
  value = "${local.app_full_name}"
}

output "vaultName" {
  value = "${module.snl-vault-api.key_vault_name}"
}

output "sharedResourceGroup" {
  value = "${local.sharedResourceGroup}"
}

output "shared_vault_uri" {
  value = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}

