package com.tsinghua.unionbackend.db.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.FileBean;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

public class FileModel extends Model {

	public FileModel() throws UnionException {
		super();
	}

	public int uploadPart(Part part, Utils utils) throws UnionException {
		try {
			String path = utils.getAttachmentPath();
			String filname = utils.getSubmittedFileName(part);
			String localName = utils.appendTimestamp(filname);
			utils.partToFile(part, path + localName);
			FileBean fil = new FileBean();
			fil.put("filename", filname);
			fil.put("url", utils.attachmentPathRelative + localName);
			String sql = fil.sqlInsert("file");
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			return res.getInt(1);
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public JSONObject allocateFile(String filename, Utils utils)
			throws UnionException {
		try {
			JSONObject ret = new JSONObject();
			String localName = utils.appendTimestamp(filename);
			String path = utils.getAttachmentPath();
			ret.put("url", utils.attachmentPathRelative + localName);
			ret.put("path", path + localName);

			FileBean fil = new FileBean();
			fil.put("filename", filename);
			fil.put("url", utils.attachmentPathRelative + localName);
			String sql = fil.sqlInsert("file");
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			ret.put("id", res.getInt(1));
			return ret;
		} catch (JSONException e) {
			throw new UnionException(e);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public FileBean getFile(int id) throws UnionException {
		try {
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery("SELECT * FROM file WHERE id = "
					+ id);
			res.next();
			return new FileBean(res);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

}
