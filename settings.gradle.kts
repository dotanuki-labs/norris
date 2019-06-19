
import modules.ModuleNames.Domain
import modules.ModuleNames.Features
import modules.ModuleNames.Infrastructure
import modules.ModuleNames.Logger
import modules.ModuleNames.MainApp

include(
    MainApp,
    Logger,
    Domain,
    Infrastructure.Networking,
    Infrastructure.Rest,
    Features.Facts,
    Features.SharedAssets
)