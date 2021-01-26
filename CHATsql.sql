USE [NewColumbus]
GO

/****** Object:  Table [dbo].[CHAT_DEMO]    Script Date: 1/27/2021 1:32:23 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[CHAT_DEMO](
	[AUTOGEN_RS_PARAM_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ID] [varchar](100) NULL,
	[KEY_NAME] [varchar](max) NULL,
	[VALUE] [varchar](20) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
