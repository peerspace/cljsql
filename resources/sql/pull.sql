--  this is a test
-- :name inquiries
select m.body from analytics.inquiries as i
left join messages as m on i.message_id=m.id
