locals {
  app_full_name = "${var.product}-${var.component}"

  events_url = "${replace(var.env, "PR-", "") != var.env ? http://snl-events-aat.service.core-compute-aat.internal : http://snl-events-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal}"
}
module "snl-api" {
  source               = "git@github.com:hmcts/moj-module-webapp"
  product              = "${var.product}-${var.component}"
  location             = "${var.location}"
  env                  = "${var.env}"
  ilbIp                = "${var.ilbIp}"
  is_frontend          = false
  subscription         = "${var.subscription}"
  additional_host_name = "${var.external_host_name}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags          = "${var.common_tags}"

  app_settings = {
   SNL_EVENTS_URL = "${local.events_url}"
  }
}
