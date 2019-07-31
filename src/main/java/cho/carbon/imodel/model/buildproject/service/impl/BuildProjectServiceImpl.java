package cho.carbon.imodel.model.buildproject.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.buildproject.dao.BuildProjectDao;
import cho.carbon.imodel.model.buildproject.service.BuildProjectService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.BasicChange;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.BasicChangeService;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.utils.FileManager;
import cho.carbon.imodel.utils.ZipCompress;
import cho.carbon.meta.enun.ModelItemType;


@Service
public class BuildProjectServiceImpl implements BuildProjectService {
	
	
	private static final String CONSTANTPATH = "\\src\\main\\java\\cho\\carbon\\biz\\constant";
	private static final String ITEMPATH = CONSTANTPATH + "\\item"; 
	@Resource
	BuildProjectDao buildProjectDao;
	
	@Resource
	BasicChangeService basicChangeService;
	
	@Resource
	ModelItemCodeGeneratorService miCodeGeneratorService;
	
	@Resource
	CommService commService;
	
	private String getProjectPath() {
		 ClassLoader classLoader = BuildProjectServiceImpl.class.getClassLoader();
		URL resource = classLoader.getResource("baseproject");
		return resource.getPath();
	}
	
	 /**
	  * 	建立项目
	  * @return
	 * @throws Exception 
	  */
	 public File buildProject(List<String> entityCodes) throws Exception {
		 
		 String  enumFileName = "EnumKeyValue";
		 String  relationTypeFileName = "RelationType";
		 String  itemFileName = "Item";
		 String  fileNmaeSuffix = ".java";
		  
		String path = getProjectPath();
		  
		// String path = "F:\\zip\\baseproject";
		String sourceFilePath = path;
		path = path.substring(0, path.length()-1);
		 String  zipFilePath = path + ".zip";
		 
		//File rootPath = new File(path);
		String constantPath = path + CONSTANTPATH;
		String itemPath = path + ITEMPATH;
		//获取枚举数据  生成文件， 写入文件夹
		 try {
			//往文件写入枚举数据
			File enumKeyFile = FileManager.createFile(constantPath, enumFileName + fileNmaeSuffix);
			createEnumFile(enumKeyFile, enumFileName);
			//往文件写入关系数据
			File relationTypeFile = FileManager.createFile(constantPath, relationTypeFileName + fileNmaeSuffix);
			createRelationFile(relationTypeFile, relationTypeFileName);
			// 往文件写入BaseContant数据
			File constantFile = FileManager.createFile(constantPath, "BaseConstant"+fileNmaeSuffix);
			getBaseConstant(constantFile, "BaseConstant");
			
			//往文件写入item数据
			for (String entityCode : entityCodes) {
				
				ModelItem modelItem = commService.get(ModelItem.class, entityCode);
				Integer type = modelItem.getType();
				ModelItemType itemType = ModelItemType.getItemType(type);
				String entityPrefix = miCodeGeneratorService.getBasicItemFix(itemType, modelItem.getBelongModel());
				
				File itemFile = FileManager.createFile(itemPath, entityCode+ itemFileName + fileNmaeSuffix);
				createItemFile(itemFile,entityCode+ itemFileName , entityCode, entityPrefix);
			}
			
			boolean isTure = zipCompress(zipFilePath, sourceFilePath);
			if (isTure) {
				File file = new File(zipFilePath);
				return file;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 return null;
	 }

	@Override
	public void createEnumFile(File file, String fileName) {
		try {
			
			basicChangeService.delete(BasicChange.CASCADEDICT);
			
			FileManager.writeFileContent(file, "package cho.carbon.biz.constant;");
			FileManager.writeFileContent(file, "public class "+fileName+" {");
			List<String> enumData = buildProjectDao.getEnumData();
			for (String data : enumData) {
				FileManager.writeFileContent(file, data);
			}
			FileManager.writeFileContent(file, "}");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createRelationFile(File file, String fileName) {
		try {
			
			basicChangeService.delete(BasicChange.RECORDRELATION);
			
			FileManager.writeFileContent(file, "package cho.carbon.biz.constant;");
			FileManager.writeFileContent(file, "public class "+fileName+" {");
			List<String> relationData = buildProjectDao.getRelationData();
			for (String data : relationData) {
				FileManager.writeFileContent(file, data);
			}
			FileManager.writeFileContent(file, "}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createItemFile(File file, String fileName, String entityCode, String entityPrefix) {
		try {
			
			basicChangeService.delete(entityCode);
			
			
			FileManager.writeFileContent(file, "package cho.carbon.biz.constant.item;");
			FileManager.writeFileContent(file, "public class "+fileName+" {");
			List<String> itemData = buildProjectDao.getItemData(entityCode, entityPrefix);
			
			for (String data : itemData) {
				FileManager.writeFileContent(file, data);
			}
			FileManager.writeFileContent(file, "}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean zipCompress(String zipFilePath, String sourceFilePath) {
		boolean isTrue = false;
		ZipCompress zipCom = new ZipCompress(zipFilePath,sourceFilePath);
        try
        {
             isTrue = zipCom.zip();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
		return isTrue;
	}

	@Override
	public boolean initializeProject() {
		String projectPath = getProjectPath();
		String path = projectPath+ CONSTANTPATH;
		boolean deleteFile = FileManager.deleteFile(path);
		return deleteFile;
	}

	@Override
	public void getBaseConstant(File file, String fileName) throws IOException {
		
		FileManager.writeFileContent(file, "package cho.carbon.biz.constant.item;");
		FileManager.writeFileContent(file, "public class BaseConstant {");
		List<String> baseConstant = buildProjectDao.getBaseConstant();
		
		String str = "public static final String COLUMN_ABP0001 = \"ABP0001\"";
		FileManager.writeFileContent(file, str);
		for (String data : baseConstant) {
			FileManager.writeFileContent(file, data);
		}
		FileManager.writeFileContent(file, "}");
	}
	
}
