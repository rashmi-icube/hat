package org.icube.hat.test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icube.hat.database.DatabaseConnectionHelper;

public class TestHelper {

	/**
	 * Retrieve the language master for the test
	 * @param companyId - company ID for db connection
	 * @return language master map
	 */
	public Map<Integer, String> getLanguageMaster(int companyId) {
		Map<Integer, String> languageMasterMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getLanguageList()}");
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				languageMasterMap.put(rs.getInt("l_id"), rs.getString("language"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the language master map", e);
		}

		return languageMasterMap;
	}

	/**
	 * Retrieve the city master for the test
	 * @param companyId - company ID for db connection
	 * @return city master map
	 */
	public Map<Integer, String> getCityMaster(int companyId, int languageId) {
		Map<Integer, String> cityMasterMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getCityList(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				cityMasterMap.put(rs.getInt("city_id"), rs.getString("city"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the city master map", e);
		}
		return cityMasterMap;
	}

	/**
	 * Get the user agreement for the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language of the test giver
	 * @return user agreement string in html format
	 */
	public String getUserAgreement(int companyId, int languageId) {
		String userAgreement = "";
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getUserAgreement(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				userAgreement = rs.getString("user_agreement");
			}
			if (userAgreement.isEmpty()) {
				throw new NullPointerException("User agreement is empty for company ID : " + companyId + " and language ID : " + languageId);
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the city master map", e);
		}
		return userAgreement;
	}

	/**
	 * Get the labels for the personal details in the language selected by the user
	 * @param companyId - company ID for db connection
	 * @param languageId - language that the labels need to be in
	 * @return labels map in the selected language
	 */
	public Map<String, String> getPersonalDetailsLabelMap(int companyId, int languageId) {
		Map<String, String> labelMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getPersonalDetailsLabelMap(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				labelMap.put("name", rs.getString("name"));
				labelMap.put("firstName", rs.getString("first_name"));
				labelMap.put("middleName", rs.getString("middle_name"));
				labelMap.put("lastName", rs.getString("last_name"));
				labelMap.put("email", rs.getString("email"));
				labelMap.put("mobile", rs.getString("mobile"));
				labelMap.put("degree", rs.getString("degree"));
				labelMap.put("stream", rs.getString("stream"));
				labelMap.put("testCity", rs.getString("test_city"));
				labelMap.put("aadharNo", rs.getString("aadhar_no"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the personal details label map", e);
		}
		return labelMap;
	}

	/**
	 * Insert the personal details of the candidate
	 * 
	 * @param companyId - company ID for db connection
	 * @param userId - unique user ID for the candidate 
	 * @param firstName - first name of the candidate
	 * @param middleName - middle name of the candidate
	 * @param lastName - last name of the candidate
	 * @param emailId - email address of the candidate
	 * @param mobileNo - mobile number of the candidate
	 * @param cityId - cityId of the candidate
	 * @param aadharNo - aadhar number of the candidate
	 * @param degree - degree of the candidate
	 * @param stream - stream of the candidate
	 * @return test info object with the test ID and candidate name for displaying purpose
	 */
	public TestInfo insertPersonalDetails(int companyId, int userId, String firstName, String middleName, String lastName, String emailId,
			String mobileNo, int cityId, String aadharNo, String degree, String stream) {
		TestInfo ti = new TestInfo();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call insertPersonalInfo(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString("firstname", firstName);
			cstmt.setString("middlename", middleName);
			cstmt.setString("lastname", lastName);
			cstmt.setString("emailid", emailId);
			cstmt.setString("mobileno", mobileNo);
			cstmt.setInt("cityid", cityId);
			cstmt.setString("aadharno", aadharNo);
			cstmt.setString("degreeip", degree);
			cstmt.setString("streamip", stream);
			cstmt.setInt("userid", userId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				ti.setTestId(rs.getInt("test_id"));
				ti.setCandidateFirstName(rs.getString("first_name"));
				ti.setCandidateMiddleName(rs.getString("middle_name"));
				ti.setCandidateLastName(rs.getString("last_name"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while storing the personal details", e);
		}
		return ti;
	}

	/**
	 * Retrieve the test instructions 
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the instructions
	 * @param testId - test ID for the instructions
	 * @return test instructions string in html format
	 */
	public String getTestInstructions(int companyId, int languageId, int testId) {
		String testInstruction = "";
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getTestInstructions(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				testInstruction = String.format(rs.getString("instructions"), testId, rs.getInt("total_questions"), rs.getString("total_time"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the test instructions for test ID : " + testId, e);
		}
		return testInstruction;
	}

	/**
	 * Get the question list for the given test ID 
	 * @param companyId - company ID for db connection
	 * @param testId - test ID 
	 * @param languageId - language in which the questions are to be returned
	 * @return question list
	 */
	public List<Question> getQuestionList(int companyId, int languageId, int testId) {
		List<Question> questionList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getQuestionList(?)}");
			cstmt.setInt("test_id", testId);
			ResultSet rs = cstmt.executeQuery();
			List<Integer> qIdList = new ArrayList<>();
			while (rs.next()) {
				qIdList.add(rs.getInt("ques_id"));
			}
			for (int qId : qIdList) {

				CallableStatement cstmt1 = dch.companySqlConnectionPool.get(companyId).prepareCall("call getQuestionDetails(?,?)");
				cstmt1.setInt("lang_id", languageId);
				cstmt1.setInt("ques_id", qId);
				rs = cstmt.executeQuery();
				while (rs.next()) {
					Question q = new Question();
					q.setQuestionId(qId);
					q.setQuestionText(rs.getString("text"));
					q.setQuestionImageId(companyId + "_" + qId);
					q.setSubQuestionList(getSubQuestionList(companyId, languageId, qId));
				}
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the question list for test ID : " + testId, e);
		}
		return questionList;
	}

	/**
	 * Get the sub question list for the specified question ID
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the question to be displayed
	 * @param questionId - question ID for the sub questions
	 * @return list of sub questions
	 */
	private List<SubQuestion> getSubQuestionList(int companyId, int languageId, int questionId) {
		List<SubQuestion> subQuestionList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getSubQuestionList(?)}");
			cstmt.setInt("ques_id", questionId);
			ResultSet rs = cstmt.executeQuery();
			List<Integer> subQueIdList = new ArrayList<>();
			while (rs.next()) {
				subQueIdList.add(rs.getInt("sub_ques_id"));
			}
			for (int sQId : subQueIdList) {

				CallableStatement cstmt1 = dch.companySqlConnectionPool.get(companyId).prepareCall("call getSubQuestionDetails(?,?)");
				cstmt1.setInt("lang_id", languageId);
				cstmt1.setInt("sub_ques_id", sQId);
				rs = cstmt.executeQuery();
				while (rs.next()) {
					SubQuestion sq = new SubQuestion();
					sq.setQuestionId(questionId);
					sq.setSubQuestionId(sQId);
					sq.setText(rs.getString("text"));
					sq.setOptionList(getOptionList(companyId, languageId, sQId));
					org.apache.log4j.Logger.getLogger(TestHelper.class).debug("Retrieved sub question with ID " + sQId);
				}
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while retrieving the sub question list for question ID : " + questionId, e);
		}

		return subQuestionList;
	}

	/**
	 * Get the options list for the sub question ID provided
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the option to be displayed in
	 * @param subQuestionId - sub question ID for which the options need to be retrieved
	 * @return list of options for the sub question
	 */
	private List<Option> getOptionList(int companyId, int languageId, int subQuestionId) {
		List<Option> optionList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getOptionList(?)}");
			cstmt.setInt("sub_ques_id", subQuestionId);
			ResultSet rs = cstmt.executeQuery();
			List<Integer> optionIdList = new ArrayList<>();
			while (rs.next()) {
				optionIdList.add(rs.getInt("opt_id"));
			}
			for (int oId : optionIdList) {

				CallableStatement cstmt1 = dch.companySqlConnectionPool.get(companyId).prepareCall("call getOptionDetails(?,?)");
				cstmt1.setInt("lang_id", languageId);
				cstmt1.setInt("opt_id", oId);
				rs = cstmt.executeQuery();
				while (rs.next()) {
					Option o = new Option();
					o.setOptionId(oId);
					o.setQuestionId(rs.getInt("ques_id"));
					o.setSubQuestionId(subQuestionId);
					o.setText(rs.getString("text"));
					org.apache.log4j.Logger.getLogger(TestHelper.class).debug("Retrieved option with ID " + oId);
				}
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while retrieving the option list for sub question ID : " + subQuestionId, e);
		}
		return optionList;
	}

	/**
	 * Updates the test status of the given test ID
	 * @param companyId - company ID for db connection
	 * @param testId - test ID for which the status needs to be updated
	 * @param status - status to be updated
	 * @return boolean value if the status was successfully updated
	 */
	public boolean updateTestStatus(int companyId, int testId, TestStatus status) {
		boolean statusUpdated = false;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call updateTestStatus(?,?)}");
			cstmt.setInt("test_id", testId);
			cstmt.setString("status", status.getValue());
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				statusUpdated = rs.getBoolean("status");
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while updating the status for test ID : " + testId, e);
		}
		return statusUpdated;
	}

	/**
	 * Saves the response for the test as a whole
	 * @param companyId - company ID for db connection
	 * @param responseList - response list for the whole test
	 * @return boolean value if the responses were saved or not
	 */
	public boolean saveResponse(int companyId, List<Response> responseList) {
		boolean responseSaved = false;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		int subQuestionId = 0;
		try {
			dch.getCompanyConnection(companyId);

			for (Response r : responseList) {
				if (r.getOptionId() > 0) {
					subQuestionId = r.getSubQuestionId();
					CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call saveResponse(?,?,?,?)}");
					cstmt.setInt("test_id", r.getTestId());
					cstmt.setInt("ques_id", r.getQuestionId());
					cstmt.setInt("sub_ques_id", r.getSubQuestionId());
					cstmt.setInt("opt_id", r.getOptionId());
					ResultSet rs = cstmt.executeQuery();
					while (rs.next()) {
						responseSaved = rs.getBoolean("status");
					}
				}
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while updating the responses for sub question ID : " + subQuestionId, e);
		}

		return responseSaved;
	}
}
